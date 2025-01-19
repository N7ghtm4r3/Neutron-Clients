package com.tecknobit.neutron.helpers

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class KReviewer actual constructor() {

    actual fun reviewInApp(
        flowAction: () -> Unit
    ) {
        flowAction()
    }

}