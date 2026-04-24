package com.tisto.kmp.helper.ui.boilerplate.sample.data.request

import com.tisto.kmp.helper.utils.model.PickedImage
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

// ══════════════════════════════════════════════════════════════════════════
// REQUEST DTO — body yang dikirim ke API (create/update).
// @Transient pickedImage tidak di-serialize — hanya untuk multipart upload.
// ══════════════════════════════════════════════════════════════════════════

@Serializable
internal data class SampleRequest(
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,
    var image: String? = null,
    var isActive: Boolean = true,
    @Transient val pickedImage: PickedImage? = null,
)
