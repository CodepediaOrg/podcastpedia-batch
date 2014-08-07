package org.podcastpedia.batch.common.entities;

import javax.persistence.AttributeConverter;

public class UpdateFrequencyConverter implements AttributeConverter<UpdateFrequency, Integer>{

	@Override
	public Integer convertToDatabaseColumn(UpdateFrequency attribute) {

		switch(attribute) {
		case DAILY: 
			return Integer.valueOf(1);
		case WEEKLY:
			return Integer.valueOf(2);
		case MONTHLY:
			return Integer.valueOf(3);
		case YEARLY:
			return Integer.valueOf(4);
		case TERMINATED:
			return Integer.valueOf(5);			
		case UNKNOWN:
			return Integer.valueOf(6);
		default: 
			throw new IllegalArgumentException("Unknown value: " + attribute);
		}
	
	}

	@Override
	public UpdateFrequency convertToEntityAttribute(Integer dbData) {
		switch (dbData){
		case 1:
		   return UpdateFrequency.DAILY;
		case 2:
		   return UpdateFrequency.WEEKLY;
		case 3:
			return UpdateFrequency.MONTHLY;
		case 4:
		   return UpdateFrequency.YEARLY;
		case 5:
		   return UpdateFrequency.TERMINATED;
		case 6:
			return UpdateFrequency.UNKNOWN;			
		default:
		   throw new IllegalArgumentException("Unknown value: " + dbData);
		  }	
	}

}
