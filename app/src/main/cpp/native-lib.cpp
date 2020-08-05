#include "geo.h"
#include <jni.h>
#include <string>

extern "C" JNIEXPORT jdouble JNICALL
Java_com_github_zhd4_fgps_MainActivity_getRandomLatitude(JNIEnv* env, jobject) {
    Geo geo;
    return geo.generateRandomLatitude();
}

extern "C" JNIEXPORT jdouble JNICALL
Java_com_github_zhd4_fgps_MainActivity_getRandomLongitude(JNIEnv* env, jobject) {
    Geo geo;
    return geo.generateRandomLongitude();
}
