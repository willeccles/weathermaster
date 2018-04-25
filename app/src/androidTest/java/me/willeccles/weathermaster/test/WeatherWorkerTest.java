package me.willeccles.weathermaster.test;

import org.junit.Before;
import org.junit.Test;

import me.willeccles.weathermaster.WeatherWorker;

import static org.junit.Assert.*;

/**
 * Created by willeccles on 4/25/18.
 */
public class WeatherWorkerTest {
	@Test
	public void getParams() throws Exception {
		assertArrayEquals(new String[]{"forecast", "BYID", "1234"}, WeatherWorker.getParams(true, 1234));
	}

	@Test
	public void getParams1() throws Exception {
		assertArrayEquals(new String[]{"current", "here", "12345", "0.0", "0.0"}, WeatherWorker.getParams(false, "here", "12345", 0.0, 0.0));
	}

	@Test
	public void kelvinToC() throws Exception {
		assertEquals(0.0, WeatherWorker.kelvinToC(273.15));
	}

	@Test
	public void kelvinToF() throws Exception {
		assertEquals(32.0, WeatherWorker.kelvinToF(273.15));
	}
}