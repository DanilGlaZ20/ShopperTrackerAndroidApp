package com.glazdan.shopperTrackerAndroidApp.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddInviteParticipantRequest(
	@SerialName("category_id")
	val categoryId: Int,
	@SerialName("participant_phone")
	val participantPhone: String,
	)

