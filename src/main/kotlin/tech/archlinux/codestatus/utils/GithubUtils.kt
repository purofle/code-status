package tech.archlinux.codestatus.utils

import org.springframework.beans.factory.annotation.Value

class GithubUtils {
	@Value("\${github.secret}")
	lateinit var secret: String


}
