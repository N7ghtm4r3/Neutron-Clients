package com.tecknobit.neutron.helpers

@Deprecated("TO USE THE BUILT-IN IN EQUINOX ONE")
fun <T> MutableCollection<T>.retainAndAdd(
    supportCollection: Collection<T>
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

@Deprecated("TO USE THE BUILT-IN IN EQUINOX ONE")
fun <T> MutableCollection<T>.mergeIfNotContained(
    mergeCollection: Collection<T>
) {
    val supportSet = this.toHashSet()
    mergeCollection.forEach { element ->
        if(!supportSet.contains(element))
            this.add(element)
    }
}