package com.tecknobit.neutron.helpers

@Deprecated("TO USE THE BUILT-IN IN EQUINOX ONE")
fun <T> MutableCollection<T>.retainAndAdd(
    supportCollection: MutableCollection<T>
) {
    val supportCollectionSet = supportCollection.toHashSet()
    this.run {
        retainAll(supportCollectionSet)
        addAll(
            supportCollectionSet.filter {
                supportItem -> supportItem !in this
            }
        )
    }
}