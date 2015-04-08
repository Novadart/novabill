package com.novadart.novabill.domain;


import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configurable
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"suffix", "business"}))
public class DocumentIDClass {

    private String suffix;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "defaultDocumentIDClass")
    private Set<Client> clients = new HashSet<>();

    @JoinColumn(name = "business")
    @ManyToOne
    private Business business;

    public boolean suffixExists(){
        String query = "select dc from DocumentIDClass dc where suffix = :suffix and dc.business.id = :id";
        return entityManager().createQuery(query, DocumentIDClass.class).
                setParameter("suffix", getSuffix()).
                setParameter("id", getBusiness().getId()).
                setFirstResult(0).setMaxResults(1).getResultList().size() == 1;
    }

    public DocumentIDClass shallowCopy(){
        DocumentIDClass newDocumentIDClass = new DocumentIDClass();
        newDocumentIDClass.setSuffix(getSuffix());
        newDocumentIDClass.setBusiness(getBusiness());
        return newDocumentIDClass;
    }

    /*
	 * Getters and setters
	 * *
	 */

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    /*
     * End of getters and setters section
     * */


    /*
     * Active record functionality
     * */

    @PersistenceContext
    transient EntityManager entityManager;

    public static final EntityManager entityManager() {
        EntityManager em = new DocumentIDClass().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countDocumentIDClasses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM DocumentIDClass o", Long.class).getSingleResult();
    }

    public static List<Client> findAllDocumentIDClasses() {
        return entityManager().createQuery("SELECT o FROM DocumentIDClass o", Client.class).getResultList();
    }

    public static DocumentIDClass findDocumentIDClass(Long id) {
        if (id == null) return null;
        return entityManager().find(DocumentIDClass.class, id);
    }

    public static List<DocumentIDClass> findDocumentIDClassEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM DocumentIDClass o", DocumentIDClass.class)
                .setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
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
            DocumentIDClass attached = DocumentIDClass.findDocumentIDClass(this.id);
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
    public DocumentIDClass merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        DocumentIDClass merged = this.entityManager.merge(this);
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
        return id;
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

    /*End of entity section*/

    public String toString() {
        return String.format("<id: %d, suffix: %s>", id, suffix);
    }


 }
