package store.type

sealed class RoadSectionType {
    data object Left : RoadSectionType()
    data object Right : RoadSectionType()
    data object Center : RoadSectionType()
}