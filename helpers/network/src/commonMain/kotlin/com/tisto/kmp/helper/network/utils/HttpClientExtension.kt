package com.tisto.kmp.helper.network.utils

import com.tisto.kmp.helper.utils.model.PickedImage
import io.github.vinceglb.filekit.readBytes
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.http.*
import io.ktor.http.content.PartData
import io.ktor.util.reflect.typeInfo
import io.ktor.utils.io.ByteReadChannel
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.*
import kotlinx.serialization.serializer


class EmptyBody()
class UnauthorizedException : Exception("Unauthorized")

suspend inline fun <reified T> HttpClient.getMethod(
    url: String,
    query: Map<String, String>? = null,
    headers: Map<String, String>? = null
): T {

//    return get(url) {
//        contentType(ContentType.Application.Json)
//        headers?.forEach { (key, value) ->
//            header(key, value)
//        }
//        query?.forEach { (k, v) -> parameter(k, v) }
//    }.body()

    val response = get(url) {
        contentType(ContentType.Application.Json)
        headers?.forEach { (key, value) -> header(key, value) }
        query?.forEach { (k, v) -> parameter(k, v) }
    }
    if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedException()
    return response.body()

}

suspend inline fun <reified Req : Any, reified Res> HttpClient.postMethod(
    url: String,
    body: Req?,
    headers: Map<String, String>? = null,
    pickedImage: PickedImage? = null,
    fileFieldName: String = "image",
): Res {

    if (pickedImage != null) {
        return requestMultipart(
            method = HttpMethod.Post,
            url = url,
            body = body,
            fileFieldName = fileFieldName,
            pickedImage = pickedImage,
            headers = headers
        )
    }

    val response = post(url) {
        contentType(ContentType.Application.Json)
        headers?.forEach { (key, value) -> header(key, value) }
        setBody(body, typeInfo<Req>()) // 🔥 INI KUNCINYA
    }
    if (response.status == HttpStatusCode.Unauthorized) throw UnauthorizedException()
    return response.body()
}

suspend inline fun <reified Req : Any, reified Res> HttpClient.putMethod(
    url: String,
    body: Req?,
    headers: Map<String, String>? = null,
    pickedImage: PickedImage? = null,
    fileFieldName: String = "image",
): Res {

    if (pickedImage != null) {
        return requestMultipart(
            method = HttpMethod.Put,
            url = url,
            body = body,
            fileFieldName = fileFieldName,
            pickedImage = pickedImage,
            headers = headers
        )
    }

    return put(url) {
        contentType(ContentType.Application.Json)
        headers?.forEach { (key, value) ->
            header(key, value)
        }
        setBody(body, typeInfo<Req>()) // 🔥 INI KUNCINYA
    }.body()
}


suspend inline fun <reified T> HttpClient.deleteMethod(
    url: String,
    headers: Map<String, String>? = null
): T {
    return delete(url) {
        contentType(ContentType.Application.Json)
        headers?.forEach { (key, value) ->
            header(key, value)
        }
    }.body()
}

fun String.pathParam(id: String): String {
    return this.replace("{id}", id)
}

suspend inline fun <reified Req : Any, reified Res : Any> HttpClient.requestMultipart(
    method: HttpMethod,
    url: String,
    body: Req? = null,
    fileFieldName: String = "image",
    pickedImage: PickedImage? = null,
    headers: Map<String, String>? = null,
): Res {

    val fields = body?.toFieldsMap() ?: emptyMap()
    val parts = mutableListOf<PartData>()

    // ✅ form fields
    fields.forEach { (k, v) ->
        parts += PartData.FormItem(
            value = v,
            dispose = {},
            partHeaders = Headers.build {
                append(HttpHeaders.ContentDisposition, "form-data; name=\"$k\"")
            }
        )
    }

    // ✅ file part
    if (pickedImage != null) {
        val bytes = pickedImage.file.readBytes()
        val fileName = pickedImage.name
        val mime = pickedImage.mimeType ?: "application/octet-stream"
        val ct = runCatching { ContentType.parse(mime) }
            .getOrElse { ContentType.Application.OctetStream }

        parts += PartData.FileItem(
            provider = { ByteReadChannel(bytes) },
            dispose = {},
            partHeaders = Headers.build {
                append(
                    HttpHeaders.ContentDisposition,
                    "form-data; name=\"$fileFieldName\"; filename=\"$fileName\""
                )
                append(HttpHeaders.ContentType, ct.toString())
            }
        )
    }

    return request(url) {
        this.method = method
        headers?.forEach { (k, v) -> header(k, v) }
        setBody(MultiPartFormDataContent(parts))
    }.body()
}

val AppJson = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
    encodeDefaults = false
}

inline fun <reified T> T.toFieldsMap(
    json: Json = AppJson
): Map<String, String> {
    val ser = json.serializersModule.serializer<T>()
    val element = json.encodeToJsonElement(ser, this).jsonObject

    return buildMap {
        element.forEach { (k, v) ->
            if (v is JsonNull) return@forEach
            val str = when (v) {
                is JsonPrimitive -> v.content
                else -> v.toString() // nested object/list jadi json string
            }
            put(k, str)
        }
    }
}