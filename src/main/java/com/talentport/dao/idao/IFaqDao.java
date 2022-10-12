package com.talentport.dao.idao;

import com.talentport.dto.Faq;
import com.talentport.dto.Idioms;

public interface IFaqDao {
	
	public Faq Create(Idioms idioms) throws Exception;
	
	public Faq Edit(Idioms idioms, String uuid) throws Exception;
	
	public Faq Delete(String uuid, int status) throws Exception;
	
	public Faq FindById(String uuid) throws Exception;
	
	public Faq FindAll() throws Exception;
	
}
