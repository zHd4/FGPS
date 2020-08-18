#include "geo.h"
#include <jni.h>
#include <string>

extern "C" JNIEXPORT jdouble JNICALL
Java_com_fgps_models_geo_Geo_randomLatitude(JNIEnv* env, jobject) {
    Geo geo;
    return geo.generateRandomLatitude();
}

extern "C" JNIEXPORT jdouble JNICALL
Java_com_fgps_models_geo_Geo_randomLongitude(JNIEnv* env, jobject) {
    Geo geo;
    return geo.generateRandomLongitude();
}
