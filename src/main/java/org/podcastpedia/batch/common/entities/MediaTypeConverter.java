package org.podcastpedia.batch.common.entities;

import javax.persistence.AttributeConverter;

public class MediaTypeConverter implements AttributeConverter<MediaType, Integer> {

	@Override
	public Integer convertToDatabaseColumn(MediaType mediaType) {

		switch(mediaType) {
		case Audio: 
			return Integer.valueOf(1);
		case Video:
			return Integer.valueOf(2);
		case VideoHD:
			return Integer.valueOf(3);
		default: 
			throw new IllegalArgumentException("Unknown value: " + mediaType);
		}
		
	}

	@Override
	public MediaType convertToEntityAttribute(Integer dbData) {		
		switch (dbData){
		case 1:
		   return MediaType.Audio;
		case 2:
		   return MediaType.Video;
		case 3:
			return MediaType.VideoHD;
		default:
		   throw new IllegalArgumentException("Unknown value: " + dbData);
		  }			
	}

}
