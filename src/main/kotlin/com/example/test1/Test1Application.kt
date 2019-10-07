package com.example.test1

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class myApplication

fun main(args: Array<String>) {
	SpringApplication.run(myApplication::class.java,*args)
}
