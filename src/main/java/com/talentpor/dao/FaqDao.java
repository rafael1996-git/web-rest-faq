package com.talentpor.dao;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.talentport.users.SecretManager;
import com.talentport.users.controller.FaqController;
import com.talentport.users.helpers.Helpers;
import com.talentport.utils.EnvironmentData;
import com.talentport.dao.idao.IFaqDao;
import com.talentport.dto.ConfigDB;
import com.talentport.dto.ContentFaq;
import com.talentport.dto.Faq;
import com.talentport.dto.Idioms;

import oracle.jdbc.OracleTypes;

import com.talentport.core.db.oracle.OracleDBPool;

@Repository
public class FaqDao implements IFaqDao {
	private static final Logger log = LoggerFactory.getLogger(FaqController.class);

	/* Create FAQ 
	 * 
	 * 1) 	Question ES
	 * 2) 	ANSWORD ES
	 * 3) 	Question EN
	 * 4) 	ANSWORD EN
	 * 
	 * 5) CURSOR
	 * */
	@SuppressWarnings("removal")
	@Override
	public Faq Create(Idioms idioms) throws Exception {
		Faq faq = new Faq();
		Connection conn =null;
		CallableStatement ps =null;
		try {
			conn = OracleDBPool.getSingletonConnectionJDBC();
			ps = conn.prepareCall("{ CALL SPFAQCRUD(NULL, ?, ?, ?, ?, '1', 'Insert', ?) }");
			
			Clob esString = conn.createClob();
			esString.setString(1, idioms.getEs().getAnsword());
			
			Clob enString = conn.createClob();
			enString.setString(1, idioms.getEn().getAnsword());
			
			/* IN */
			ps.setString(1, idioms.getEs().getQuestion().toString());
			ps.setClob(2, esString);
			ps.setString(3, idioms.getEn().getQuestion().toString());
			ps.setClob(4, enString);
			
			/* OUT */
			ps.registerOutParameter(5, OracleTypes.CURSOR);
			
			ps.execute();
			
			ResultSet resultSet = (ResultSet) ps.getObject(5);
			List<ContentFaq> fqEn = new ArrayList<ContentFaq>();
			List<ContentFaq> fqEs = new ArrayList<ContentFaq>();
			while (resultSet.next()) {
				ContentFaq en = new ContentFaq();
				ContentFaq es = new ContentFaq();
				es.setQuestion(resultSet.getString("PREGUNTAES"));
				es.setAnsword(resultSet.getString("RESPUESTAES"));
				en.setQuestion(resultSet.getString("PREGUNTAEN"));
				en.setAnsword(resultSet.getString("RESPUESTAES"));
				faq.setStatus(Integer.parseInt(resultSet.getString("ACTIVO")));
				fqEs.add(es);
				fqEn.add(en);
			}
			faq.setEn(fqEn);
			faq.setEs(fqEs);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}finally{
			try {
				ps.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return faq;
	}
	
	/* Edit  FAQ 
	 * 1)	ID FAQ
	 * 2) 	Question ES
	 * 3) 	ANSWORD ES
	 * 4) 	Question EN
	 * 5) 	ANSWORD EN
	 * 
	 * 6) CURSOR
	 * */
	@Override
	public Faq Edit(Idioms idioms, String uuid) throws Exception {
		Connection conn =null;
		CallableStatement ps =null;
		Faq faq = new Faq();
		try {
			conn = OracleDBPool.getSingletonConnectionJDBC();
			ps = conn.prepareCall("{ CALL SPFAQCURD(?, ?, ?, ?, ?, '1', 'Update', ?) }");
			
			Clob esString = conn.createClob();
			esString.setString(1, idioms.getEs().getAnsword());
			
			Clob enString = conn.createClob();
			enString.setString(1, idioms.getEn().getAnsword());
			
			/* IN */
			ps.setString(1, uuid);
			ps.setString(2, idioms.getEs().getQuestion());
			ps.setClob(3, esString);
			ps.setString(4, idioms.getEn().getQuestion());
			ps.setClob(5, enString);
			
			/* OUT */
			ps.registerOutParameter(6, OracleTypes.CURSOR);
			
			ps.executeUpdate();
			
			ResultSet resultSet = (ResultSet) ps.getObject(6);
			List<ContentFaq> fqEn = new ArrayList<ContentFaq>();
			List<ContentFaq> fqEs = new ArrayList<ContentFaq>();
			while (resultSet.next()) {
				ContentFaq en = new ContentFaq();
				ContentFaq es = new ContentFaq();
				es.setQuestion(resultSet.getString("PREGUNTAES"));
				es.setAnsword(resultSet.getString("RESPUESTAES"));
				en.setQuestion(resultSet.getString("PREGUNTAEN"));
				en.setAnsword(resultSet.getString("RESPUESTAES"));
				faq.setStatus(Integer.parseInt(resultSet.getString("ACTIVO")));
				fqEs.add(es);
				fqEn.add(en);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}finally{
			try {
				ps.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return faq;
	}
	
	/* Delete  FAQ 
	 * 1)	ID FAQ
	 * 2) 	Question ES
	 * 3) 	ANSWORD ES
	 * 4) 	Question EN
	 * 5) 	ANSWORD EN
	 * 
	 * 6) CURSOR
	 * */
	@Override
	public Faq Delete(String uuid, int status) throws Exception {
		Connection conn =null;
		CallableStatement ps =null;
		Faq faq = new Faq();
		try {
			conn = OracleDBPool.getSingletonConnectionJDBC();
			ps = conn.prepareCall("{ CALL SPFAQCURD(?, NULL, NULL, NULL, NULL, ?, 'Delete', ?) }");
			
			/* IN */
			ps.setString(1, uuid);
			ps.setString(2, String.valueOf(status));
			
			/* OUT */
			ps.registerOutParameter(3, OracleTypes.CURSOR);
			
			ps.executeUpdate();
			
			ResultSet resultSet = (ResultSet) ps.getObject(6);
			List<ContentFaq> fqEn = new ArrayList<ContentFaq>();
			List<ContentFaq> fqEs = new ArrayList<ContentFaq>();
			while (resultSet.next()) {
				ContentFaq en = new ContentFaq();
				ContentFaq es = new ContentFaq();
				es.setQuestion(resultSet.getString("PREGUNTAES"));
				es.setAnsword(resultSet.getString("RESPUESTAES"));
				en.setQuestion(resultSet.getString("PREGUNTAEN"));
				en.setAnsword(resultSet.getString("RESPUESTAES"));
				faq.setStatus(Integer.parseInt(resultSet.getString("ACTIVO")));
				fqEs.add(es);
				fqEn.add(en);
			}
			faq.setEn(fqEn);
			faq.setEs(fqEs);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}finally{
			try {
				ps.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return faq;
	}
	
	/* FIND ID */
	@Override
	public Faq FindById(String uuid) throws Exception {
		Connection conn =null;
		CallableStatement ps =null;
		Faq faq = new Faq();
		try {

			conn = OracleDBPool.getSingletonConnectionJDBC();
			ps = conn.prepareCall("{ CALL SPFAQCURD(?, NULL, NULL, NULL, NULL, NULL, 'SelectOne', ?) }");
			
			/* IN */
			ps.setString(1, uuid);
			
			/* OUT */
			ps.registerOutParameter(2, OracleTypes.CURSOR); 
			
			ps.execute();
				
			ResultSet resultSet = (ResultSet) ps.getObject(2);
			List<ContentFaq> fqEn = new ArrayList<ContentFaq>();
			List<ContentFaq> fqEs = new ArrayList<ContentFaq>();
			while (resultSet.next()) {
				ContentFaq en = new ContentFaq();
				ContentFaq es = new ContentFaq();
				es.setQuestion(resultSet.getString("PREGUNTAES"));
				es.setAnsword(resultSet.getString("RESPUESTAES"));
				en.setQuestion(resultSet.getString("PREGUNTAEN"));
				en.setAnsword(resultSet.getString("RESPUESTAES"));
				faq.setStatus(Integer.parseInt(resultSet.getString("ACTIVO")));
				fqEs.add(es);
				fqEn.add(en);
			}
			faq.setEn(fqEn);
			faq.setEs(fqEs);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}finally{
			try {
				ps.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return faq;
	}
	
	/* Find ALL */
	@Override
	public Faq FindAll() throws Exception {
		Connection conn =null;
		CallableStatement ps =null;
		Faq faq = new Faq();
		try {

			conn = OracleDBPool.getSingletonConnectionJDBC();
			ps = conn.prepareCall("{ CALL SPFAQCRUD(NULL, NULL, NULL, NULL, NULL, NULL, 'Select', ?) }");
			
			/* IN */
			
			/* OUT */
			ps.registerOutParameter(1, OracleTypes.CURSOR); 
			
			ps.execute();
			
			ResultSet resultSet = (ResultSet) ps.getObject(1);
			List<ContentFaq> fqEn = new ArrayList<ContentFaq>();
			List<ContentFaq> fqEs = new ArrayList<ContentFaq>();
			while (resultSet.next()) {
				ContentFaq en = new ContentFaq();
				ContentFaq es = new ContentFaq();
				es.setQuestion(resultSet.getString("PREGUNTAES"));
				es.setAnsword(resultSet.getString("RESPUESTAES"));
				en.setQuestion(resultSet.getString("PREGUNTAEN"));
				en.setAnsword(resultSet.getString("RESPUESTAES"));
				faq.setStatus(Integer.parseInt(resultSet.getString("ACTIVO")));
				fqEs.add(es);
				fqEn.add(en);
			}
			faq.setEn(fqEn);
			faq.setEs(fqEs);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}finally{
			try {
				ps.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return faq;
	}
	
	static {
		try {
			
			String secretBD = SecretManager.getParameter(EnvironmentData.getPropertyValue("OraDBKey"));
			//String secretBD = SecretManager.getParameter("com/talentport/db/oracle/USRTALENTPORTTST");
			Gson gson = new Gson();
			ConfigDB cognitoConfig = gson.fromJson(secretBD, ConfigDB.class);
			OracleDBPool.initSingletonConnectionCredentials(cognitoConfig.getUrl(), cognitoConfig.getUser(),
					cognitoConfig.getPass());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
