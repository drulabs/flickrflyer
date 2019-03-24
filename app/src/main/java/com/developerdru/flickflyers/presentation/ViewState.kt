package com.developerdru.flickflyers.presentation

class ViewState<T>(
    val data: T?,
    val loading: Boolean,
    val error: Throwable?,
    var viewType: ViewType = ViewType.LIST
)

enum class ViewType {
    LIST, GRID
}