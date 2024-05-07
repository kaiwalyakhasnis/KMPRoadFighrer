package store.type

sealed class ControllerButton {
    data object Left: ControllerButton()
    data object Right: ControllerButton()
    data object None: ControllerButton()
}