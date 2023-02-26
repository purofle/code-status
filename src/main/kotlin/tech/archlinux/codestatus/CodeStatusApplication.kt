package tech.archlinux.codestatus

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan

//@ServletComponentScan
@SpringBootApplication
class CodeStatusApplication

fun main(args: Array<String>) {
	runApplication<CodeStatusApplication>(*args)
}
