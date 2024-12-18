package com.rentread.api.enumeration;

import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BookStatus {
	AVAILABLE("AVAILABLE"),
	NOTAVAILABLE("NOT_AVAILABLE");
	
	private final String value;

    public static Role get(final String name) {
        return Stream.of(Role.values())
            .filter(p -> p.name().equals(name.toUpperCase()) || p.getValue().equals(name.toUpperCase()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("Invalid badge name: %s", name)));
    }
}
