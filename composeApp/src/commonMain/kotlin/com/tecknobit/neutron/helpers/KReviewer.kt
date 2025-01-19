package com.tecknobit.neutron.helpers


@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class KReviewer() {

    fun reviewInApp(
        flowAction: () -> Unit
    )

}