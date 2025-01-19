package com.tecknobit.neutron.helpers

import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.tecknobit.equinoxcompose.utilities.context.ContextActivityProvider

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class KReviewer actual constructor() {

    /**
     * `activity` the activity where the review helper has been instantiated
     */
    private val activity = ContextActivityProvider.getCurrentActivity()!!

    /**
     * `reviewManager` the manager used to allow the user to review the application in app
     */
    private var reviewManager: ReviewManager = ReviewManagerFactory.create(activity)

    actual fun reviewInApp(
        flowAction: () -> Unit
    ) {
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val flow = reviewManager.launchReviewFlow(activity, task.result)
                flow.addOnCompleteListener {
                    flowAction()
                }
                flow.addOnCanceledListener {
                    flowAction()
                }
            } else
                flowAction()
        }
        request.addOnFailureListener {
            flowAction()
        }
    }

}