package com.tisto.kmp.helper.ui.boilerplate.sample.data.model

import kotlinx.serialization.Serializable

// ══════════════════════════════════════════════════════════════════════════
// DOMAIN MODEL — satu @Serializable data class = DTO + domain model.
// Tidak perlu SampleDto + toDomain(). Tidak perlu SampleDraft.
// ══════════════════════════════════════════════════════════════════════════

@Serializable
internal data class Sample(
    var id: String = "",
    var outletId: String? = null,
    var name: String = "",
    var description: String? = null,
    var image: String? = null,
    var createdAt: String? = null,
    var updatedAt: String? = null,
    var isActive: Boolean = true,
)
