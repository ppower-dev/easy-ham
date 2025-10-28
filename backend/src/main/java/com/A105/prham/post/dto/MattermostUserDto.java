package com.A105.prham.post.dto;

public record MattermostUserDto(
	String nickname,

	String username
) {
	public String getNicknameToUse() {
		if(nickname != null && !nickname.isBlank()) {
			return nickname;
		}
		return username;
	}
}
