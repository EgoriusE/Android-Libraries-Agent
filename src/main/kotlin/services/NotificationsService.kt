package services


import com.intellij.openapi.project.Project
import utils.NotificationsFactory


private const val NOTIFICATIONS_ID = "com.egorius.android.projectgenerator"
private const val DEFAULT_NOTIFICATIONS_TITLE = "Title will be here"
private const val INFO_NOTIFICATION_GROUP_ID = "$NOTIFICATIONS_ID.info"
private const val ERROR_NOTIFICATION_GROUP_ID = "$NOTIFICATIONS_ID.error"


fun Project.balloonInfo(title: String = DEFAULT_NOTIFICATIONS_TITLE, message: String) {
    NotificationsFactory.balloonInfo(
        project = this,
        infoNotificationGroupId = INFO_NOTIFICATION_GROUP_ID,
        title = title,
        message = message
    )
}

fun Project.balloonError(title: String = DEFAULT_NOTIFICATIONS_TITLE, message: String) {
    NotificationsFactory.balloonError(
        project = this,
        errorNotificationGroupId = ERROR_NOTIFICATION_GROUP_ID,
        title = title,
        message = message
    )
}