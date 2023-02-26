package tech.archlinux.codestatus.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HMACTest {

	@Test
	fun calculateHMac() {
		assertEquals(
			HMAC.calculateHMac(
				secret = "123456",
				data = "test"
			),
			"9d2bb116e1df997ffe8a5139fc1d187f976c19579a138414a112bc2e39020eba"
		)
	}
}
