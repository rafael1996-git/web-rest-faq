package com.talentport.dto;

import java.util.List;

public class Faq {
	private List<ContentFaq> es;
	
	private List<ContentFaq> en;
	
	private int status;

	public List<ContentFaq> getEs() {
		return es;
	}

	public void setEs(List<ContentFaq> es) {
		this.es = es;
	}

	public List<ContentFaq> getEn() {
		return en;
	}

	public void setEn(List<ContentFaq> en) {
		this.en = en;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
