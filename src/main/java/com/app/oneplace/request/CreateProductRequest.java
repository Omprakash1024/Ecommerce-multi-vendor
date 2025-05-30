package com.app.oneplace.request;

import java.util.List;

import lombok.Data;

@Data
public class CreateProductRequest {

	private String title;
	private String description;
	private int mrpPrice;
	private int sellingPrice;
	private String color;
	private List<String> images;
	private String category; // male/female/kids
	private String category2; //second level category - top wear//bottom wear
	private String category3; // main category - men shirt/women tops/kids pant
	private String size;
}
