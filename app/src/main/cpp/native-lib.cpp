#include <jni.h>
#include <string>

extern "C" {
	JNIEXPORT jstring JNICALL
	Java_me_willeccles_weathermaster_MainActivity_stringFromJNI(
			JNIEnv *env,
			jobject /* this */) {
		std::string hello = "Hello from C++";
		return env->NewStringUTF(hello.c_str());
	}

	JNIEXPORT jdouble JNICALL
	Java_me_willeccles_weathermaster_WeatherWorker_kelvinToC(JNIEnv *env, jclass type, jdouble k) {
		return k - 273.15;
	}

	JNIEXPORT jdouble JNICALL
	Java_me_willeccles_weathermaster_WeatherWorker_kelvinToF(JNIEnv *env, jclass type, jdouble k) {
		return k * 9.0 / 5.0 - 459.67;
	}
}