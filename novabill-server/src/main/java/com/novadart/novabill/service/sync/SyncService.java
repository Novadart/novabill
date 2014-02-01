package com.novadart.novabill.service.sync;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import com.novadart.novabill.android.shared.dto.SyncDeltaStateDTO;
import com.novadart.novabill.service.UtilsService;

@Service
public class SyncService {

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private UtilsService utilsService;

	public SyncDeltaStateDTO getDeltaState(final Long mark) {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		StringBuilder sqlBuilder = new StringBuilder("select * from audit_log where business = ?"); 
		if (mark != null)
			sqlBuilder.append(" and id > ?");
		sqlBuilder.append(" order by id asc");
		VersionableStateRowCallbackHandler rowCallbackHandler = new VersionableStateRowCallbackHandler();
		template.query(sqlBuilder.toString(), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement stmt) throws SQLException {
				stmt.setLong(1, utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId());
				if (mark != null)
					stmt.setLong(2, mark);
			}
		}, rowCallbackHandler);
		return rowCallbackHandler.getResult();
	}

}
