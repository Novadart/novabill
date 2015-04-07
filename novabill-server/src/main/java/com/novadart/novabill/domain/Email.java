package com.novadart.novabill.domain;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.novadart.novabill.annotation.Trimmed;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Configurable
@Entity
public class Email implements Serializable {

	private static final long serialVersionUID = -3446297950310934579L;
	
	public static final int SUBJECT_MAX_LENGTH = 78;
	
	public static final int TEXT_MAX_LENGTH = 15_000;
	
	public static final int EMAIL_MAX_LENGTH = 255;
	
	
	@Autowired
	private transient JavaMailSender mailSender;
	
	@Column(name = "to_addr")
	@Size(max = EMAIL_MAX_LENGTH)
	@Trimmed
	private String to;
	
	@Column(name = "from_addr")
	@Size(max = EMAIL_MAX_LENGTH)
	@Trimmed
	private String from;

	@Size(max = SUBJECT_MAX_LENGTH)
	private String subject;
	
	@Size(max = TEXT_MAX_LENGTH)
	private String text;
	
	@Column(name = "replyto_addr")
	@Size(max = EMAIL_MAX_LENGTH)
	@Trimmed
	private String replyTo;

	@Basic(fetch = FetchType.LAZY)
    private byte[] attachment;
	
	private String attachmentName;
	
	private EmailStatus status;
	
	private int tries;
	
	public Email(){}
	
	public Email(String[] to, String from, String subject, String text, String replyTo, byte[] attachment, String attachmentName) {
		this.setTo(to);
		this.from = from;
		this.subject = subject;
		this.text = text;
		this.replyTo = replyTo;
		this.attachment = attachment;
		this.attachmentName = attachmentName;
		tries = 0;
		status = EmailStatus.PENDING;
	}
	
	public Email(String[] to, String from, String subject, String text, String replyTo) {
		this(to, from, subject, text, replyTo, null, null);
	}
	
	public Email(String[] to, String from, String subject, String text) {
		this(to, from, subject, text, null, null, null);
	}

	public void send() throws MessagingException{
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		boolean hasAttachment = attachment != null && StringUtils.isNotBlank(attachmentName); 
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, hasAttachment, CharEncoding.UTF_8);
		messageHelper.setTo(getTo());
		messageHelper.setFrom(getFrom());
		messageHelper.setSubject(getSubject());
		messageHelper.setText(getText(), true);
		if(StringUtils.isNotBlank(getReplyTo()))
			messageHelper.setReplyTo(getReplyTo());
		if(hasAttachment)
			messageHelper.addAttachment(attachmentName, new ByteArrayResource(attachment));
		mailSender.send(mimeMessage);
	}
	
	/*
	 * Getters and Setters section
	 */
	
	public String[] getTo() {
		List<String> r = new ArrayList<>();
		for(String email: Splitter.on(";").split(this.to))
			r.add(email);
		return r.toArray(new String[r.size()]);
	}

	public void setTo(String[] to) {
		this.to = Joiner.on(";").join(to);
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	public byte[] getAttachment() {
		return attachment;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public EmailStatus getStatus() {
		return status;
	}

	public void setStatus(EmailStatus status) {
		this.status = status;
	}

	public int getTries() {
		return tries;
	}

	public void setTries(int tries) {
		this.tries = tries;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	/*
	 * End of Getters and Setters section
	 */
	
	/*
     * Active record functionality
     * */
    
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new Email().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countEmails() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Email o", Long.class).getSingleResult();
    }
    
    public static List<Email> findAllEmails() {
        return entityManager().createQuery("SELECT o FROM Email o", Email.class).getResultList();
    }
    
    public static Email findEmail(Long id) {
        if (id == null) return null;
        return entityManager().find(Email.class, id);
    }
    
    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
        	Email attached = Email.findEmail(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Email merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Email merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    /*
     * End of active record functionality section
     * */
	
	
    /*
     * Entity
     * */
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    
    @Version
    @Column(name = "version")
    private Integer version;
    
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }
    
    /*
     * End of entity section
     * */
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
	
}
