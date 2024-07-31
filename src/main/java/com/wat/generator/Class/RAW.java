package com.wat.generator.Class;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RAW {
	private RAW_HEADER HEADER;
	private List<RAW_BODY> BODY;
}
