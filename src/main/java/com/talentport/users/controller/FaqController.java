package com.talentport.users.controller;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.talentpor.dao.FaqDao;
import com.talentport.dao.idao.IFaqDao;
import com.talentport.dto.ContentFaq;
import com.talentport.dto.Faq;
import com.talentport.dto.Idioms;
import com.talentport.dto.Response;
import com.talentport.users.helpers.Helpers;

@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE })
@RestController
@EnableWebMvc
@RequestMapping("/faq")
public class FaqController {

	private static final Logger logger = LoggerFactory.getLogger(FaqController.class);

	@Autowired(required = false)
	private BCryptPasswordEncoder pswEncoder;

	@Autowired(required = false)
	private IFaqDao service;


	private AmazonS3 amazonS3Client = null;

	/* Details User */
	@RequestMapping(path = "/detail/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> Detail(@PathVariable String id) throws Exception {

		service = new FaqDao();

		Map<String, Object> dataResponse = new HashMap<>();
		Response response = new Response();

		try {

			Faq faq = service.FindById(id);

			if (faq == null) {
				response.setCode(400);
				response.setMessage("Error: could not edit, faq ID: " + id + " does not exist in the database!");

			}  else {

				dataResponse.put("ES", faq.getEs());
				dataResponse.put("EN", faq.getEn());

				response.setCode(200);
				response.setMessage("The client has been successfully!");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.setCode(500);
			response.setMessage("");
			response.setError(e.getMessage().toString());

		}
		return Helpers.ResponseClass(response.getCode(), dataResponse, response.getMessage(), response.getError());
	}

	/* Create User */
	@RequestMapping(path = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> Create(@RequestBody Idioms idioms) throws Exception {

		service = new FaqDao();

		Map<String, Object> dataResponse = new HashMap<>();
		Response response = new Response();

		try {

			Faq faq = service.Create(idioms);

			dataResponse.put("ES", faq.getEs());
			dataResponse.put("EN", faq.getEn());

			response.setCode(200);
			response.setMessage("Successful Faq Creation");

		} catch (Exception e) {
			logger.error(e.getMessage());
			response.setCode(500);
			response.setMessage("Error performing database insert");
			response.setError(e.getMessage());
		}
		return Helpers.ResponseClass(response.getCode(), dataResponse, response.getMessage(), response.getError());
	}

	/* Edit User */
	@RequestMapping(path = "/delete/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> Delete(@RequestBody Idioms idioms, @PathVariable String id) throws Exception {

		service = new FaqDao();

		Map<String, Object> dataResponse = new HashMap<>();
		Response response = new Response();
		try {
			Faq faqs = service.FindById(id);

			if (faqs == null) {
				response.setCode(400);
				response.setMessage("Error: could not edit, faq ID: " + id + " does not exist in the database!");

			}  else {
				Faq faq = service.Delete(id, faqs.getStatus());
				dataResponse.put("ES", faq.getEs());
				dataResponse.put("EN", faq.getEn());

				response.setCode(200);
				response.setMessage("The faq delete has been successfully!");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.setCode(500);
			response.setMessage("");
			response.setError(e.getMessage().toString());

		}
		return Helpers.ResponseClass(response.getCode(), dataResponse, response.getMessage(), response.getError());
	}

	/* Edit User */
	@RequestMapping(path = "/edit/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> Update(@RequestBody Idioms idioms, @PathVariable String id) throws Exception {

		service = new FaqDao();

		Map<String, Object> dataResponse = new HashMap<>();
		Response response = new Response();

		try {
			Faq faq = service.FindById(id);

			if (faq == null) {
				response.setCode(400);
				response.setMessage("Error: could not edit, faq ID: " + id + " does not exist in the database!");

			}  else {

				dataResponse.put("ES", faq.getEs());
				dataResponse.put("EN", faq.getEn());

				response.setCode(200);
				response.setMessage("The faq edit has been successfully!");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.setCode(500);
			response.setMessage("");
			response.setError(e.getMessage().toString());

		}
		return Helpers.ResponseClass(response.getCode(), dataResponse, response.getMessage(), response.getError());
	}

	/* Details User */
	@RequestMapping(path = "/list", method = RequestMethod.GET)
	public ResponseEntity<?> List() throws Exception {

		service = new FaqDao();

		Map<String, Object> dataResponse = new HashMap<>();
		Response response = new Response();
		List<ContentFaq> es = new ArrayList<>();
		List<ContentFaq> en = new ArrayList<>();

		try {

			//Faq faq = service.FindAll();

			// Reemplazar por las plantillas
			initializeAmazons3Client();
			for (int i = 1; i < 10; i++) {
				S3Object preguntaEs = amazonS3Client.getObject("dev.talentport.faq", "pregunta"+i+"-es.txt");
				S3Object respuestaEs = amazonS3Client.getObject("dev.talentport.faq", "respuesta"+i+"-es.txt");
				S3Object preguntaEn = amazonS3Client.getObject("dev.talentport.faq", "pregunta"+i+"-en.txt");
				S3Object respuestaEn = amazonS3Client.getObject("dev.talentport.faq", "respuesta"+i+"-en.txt");

				InputStream contentPreguntaEs = preguntaEs.getObjectContent();
				InputStream contentRespuestaEs = respuestaEs.getObjectContent();
				InputStream contentPreguntaEn = preguntaEn.getObjectContent();
				InputStream contentRespuestaEn = respuestaEn.getObjectContent();

				StringWriter writerPreguntaEs = new StringWriter();
				StringWriter writerRespuestaEs = new StringWriter();
				StringWriter writerPreguntaEn = new StringWriter();
				StringWriter writerRespuestaEn = new StringWriter();

				IOUtils.copy(contentPreguntaEs, writerPreguntaEs, "UTF-8");
				IOUtils.copy(contentRespuestaEs, writerRespuestaEs, "UTF-8");
				IOUtils.copy(contentPreguntaEn, writerPreguntaEn, "UTF-8");
				IOUtils.copy(contentRespuestaEn, writerRespuestaEn, "UTF-8");

				ContentFaq faqItemEs = new ContentFaq();
				ContentFaq faqItemEn = new ContentFaq();

				faqItemEs.setAnsword(writerRespuestaEs.toString());
				faqItemEs.setQuestion(writerPreguntaEs.toString());
				faqItemEn.setAnsword(writerRespuestaEn.toString());
				faqItemEn.setQuestion(writerPreguntaEn.toString());

				es.add(faqItemEs);
				en.add(faqItemEn);
			}

			dataResponse.put("ES", es);
			dataResponse.put("EN", en);
			response.setCode(200);
			response.setMessage("The faq has been successfully!");
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.setCode(500);
			response.setMessage("");
			response.setError(e.getMessage().toString());

		}
		return Helpers.ResponseClass(response.getCode(), dataResponse, response.getMessage(), response.getError());
	}

	private void initializeAmazons3Client() throws Exception {

		if (amazonS3Client == null) {
			amazonS3Client = AmazonS3ClientBuilder.standard().build();
		}
	}
}
