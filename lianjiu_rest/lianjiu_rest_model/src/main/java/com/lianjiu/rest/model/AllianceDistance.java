package com.lianjiu.rest.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianjiu.model.Waste;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AllianceDistance extends Waste {
	private Double distance;
	private String allianceId;

	public AllianceDistance() {
	}

	public AllianceDistance(Double distance, String allianceId) {
		this.distance = distance;
		this.allianceId = allianceId;
	}

	public Double getwDistance() {
		return distance;
	}

	public void setwDistance(Double distance) {
		this.distance = distance;
	}

	public String getAllianceId() {
		return allianceId;
	}

	public void setAllianceId(String allianceId) {
		this.allianceId = allianceId;
	}

}
