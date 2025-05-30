#include <jni.h>
#include <string>
#include "obfuscate.h"

using namespace std;

extern "C" JNIEXPORT jstring JNICALL
Java_com_sameer_arora_MainActivity_Telegram(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF(OBFUSCATE("https://telegram.me/gameblaster0pro"));
}