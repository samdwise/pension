package ng.com.justjava.epayment.model;

import java.io.*;
import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.jpa.*;

import com.googlecode.jcsv.*;
import com.googlecode.jcsv.annotations.*;
import com.googlecode.jcsv.annotations.internal.*;
import com.googlecode.jcsv.reader.*;
import com.googlecode.jcsv.reader.internal.*;

@Entity
public class Bank {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	@MapToColumn(columnName="name")
	private String name;
	@MapToColumn(columnName="code")
	private String code;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public static void saveUpload(Reader csvFile,CSVStrategy strategy,ValueProcessorProvider vpp){		
		try {

			CSVReaderBuilder<Bank> builder = new CSVReaderBuilder<Bank>(csvFile);

			builder.strategy(strategy);
			CSVReader<Bank> csvReader = builder.entryParser(	new AnnotationEntryParser<Bank>(
					Bank.class, vpp)).build();
			
			List<Bank> banks = csvReader.readAll();
			for (Bank bank : banks) {
				XPersistence.getManager().merge(bank);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
