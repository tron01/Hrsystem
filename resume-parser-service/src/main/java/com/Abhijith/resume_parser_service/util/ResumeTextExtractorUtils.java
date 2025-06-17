package com.Abhijith.resume_parser_service.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResumeTextExtractorUtils {
	
	private static final List<String> COMMON_SKILLS = List.of(
			"Java", "Spring", "Spring Boot", "Hibernate", "MySQL",
			"MongoDB", "Docker", "Kubernetes", "Git", "Kafka", "AWS", "Python", "JavaScript", "React", "Node.js"
	);
	
	public static String extractEmail(String text) {
		Matcher matcher = Pattern.compile("[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}").matcher(text);
		return matcher.find() ? matcher.group() : "unknown@example.com";
	}
	
	public static String extractPhone(String text) {
		Matcher matcher = Pattern.compile("(\\+\\d{1,2}\\s?)?(\\d{10})").matcher(text);
		return matcher.find() ? matcher.group() : "unknown";
	}
	
	public static String extractName(String text) {
		return text.split("\n")[0].trim();
	}
	
	public static List<String> extractSkills(String text) {
		Set<String> matched = new HashSet<>();
		String lowerText = text.toLowerCase();
		for (String skill : COMMON_SKILLS) {
			if (lowerText.contains(skill.toLowerCase())) {
				matched.add(skill);
			}
		}
		return new ArrayList<>(matched);
	}
	
	public static String extractEducation(String text) {
		if (text.toLowerCase().contains("bachelor")) return "Bachelor's Degree";
		if (text.toLowerCase().contains("master")) return "Master's Degree";
		if (text.toLowerCase().contains("phd")) return "PhD";
		return "Not mentioned";
	}
	
	public static String extractExperience(String text) {
		// Dummy check: Better with NLP
		if (text.toLowerCase().contains("experience")) {
			return "Experience section found";
		}
		return "Not mentioned";
	}
}