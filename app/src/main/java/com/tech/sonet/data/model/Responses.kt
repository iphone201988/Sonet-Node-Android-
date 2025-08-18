package com.tech.sonet.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**************************   card upload   api response*********************/


data class CardUploadApiResponse(
    var `data`: CardUploadApiResponseData?,
    var message: String?,
    var status: Int?
) {
    data class CardUploadApiResponseData(
        var __v: Int?,
        var _id: String?,
        var categoryId: String?,
        var createdAt: String?,
        var description: String?,
        var isOnline: Int?,
        var is_active: Int?,
        var level: Int?,
        var location: Location?,
        var post_image: String?,
        var social_media: String?,
        var title: String?,
        var updatedAt: String?,
        var userId: String?
    ) {
        data class Location(
            var coordinates: List<Double?>?,
            var type: String?
        )
    }
}


/**************************   forgot password  api response*********************/

data class ForgotPasswordApiResponse(
    var `data`: Data?,
    var message: String?,
    var status: Int?
) {
    data class Data(
        var session: String?
    )
}

/**************************   check otp  api response*********************/

data class OtpVerifyResponse(
    var `data`: String?,
    var message: String?,
    var status: Int?
)

/**************************   get my card  api response*********************/

//@Parcelize
//data class GetMyCardApiResponse(
//    var `data`: List<GetMyCardApiResponseData?>?,
//    var links: Links?,
//    var message: String?,
//    var status: Int?
//) : Parcelable{
//    @Parcelize
//    data class GetMyCardApiResponseData(
//        var __v: Int?,
//        var _id: String?,
//        var categoryId: String?,
//        var createdAt: String?,
//        var categoryDetail:,
//        var description: String?,
//        var isOnline: Int?,
//        var is_active: Int?,
//        var levelId: String?,
//        var location: Location?,
//        var post_image: String?,
//        var social_media: String?,
//        var title: String?,
//        var updatedAt: String?,
//        var userId: String?
//    ) : Parcelable {
//        @Parcelize
//        data class Location(
//            var coordinates: List<Double?>?,
//            var type: String?
//        ): Parcelable
//    }
//   @Parcelize
//    data class Links(
//        var currentPage: Int?,
//        var total: Int?,
//        var totalPages: Int?
//    ) : Parcelable
//}


@Parcelize
data class GetMyCardApiResponse(
    var `data`: List<GetMyCardApiResponseData?>?,
    var links: Links?,
    var message: String?,
    var status: Int?
) : Parcelable {
    @Parcelize
    data class GetMyCardApiResponseData(
        var __v: Int?,
        var _id: String?,
        var categoryDetail: CategoryDetail?,
        var categoryId: String?,
        var createdAt: String?,
        var description: String?,
        var isOnline: Int?,
        var mobile: String?,
        var is_active: Int?,
        var levelId: String?,
        var levelDetail: LevelsDetail?,
        var location: Location?,
        var post_image: String?,
        var social_media: String?,
        var title: String?,
        var updatedAt: String?,
        var userId: String?
    ) : Parcelable {
        @Parcelize
        data class CategoryDetail(
            var __v: Int?,
            var _id: String?,
            var adminId: String?,
            var category: String?,
            var category_image: String?,
            var category_name: String?,
            var createdAt: String?,
            var social_media: String?,
            var status: Int?,
            var updatedAt: String?
        ): Parcelable

        @Parcelize
        data class LevelsDetail(
            var __v: Int?,
            var _id: String?,
            var createdAt: String?,
            var is_active: Int?,
            var level: String?,
            var updatedAt: String?
        ): Parcelable

        @Parcelize
        data class Location(
            var coordinates: List<Double?>?,
            var type: String?
        ) :Parcelable
    }

    @Parcelize
    data class Links(
        var currentPage: Int?,
        var total: Int?,
        var totalPages: Int?
    ) :Parcelable
}

/**************************   get category api  response*********************/


data class GetCategoryApiResponse(
    var `data`: List<GetCategoryApiResponseData?>?,
    var message: String?,
    var status: Int?
) {
    data class GetCategoryApiResponseData(
        var __v: Int?,
        var _id: String?,
        var adminId: String?,
        var category: String?,
        var category_image: String?,
        var category_name: String?,
        var category_short_name: String?,
        var createdAt: String?,
        var social_media: String?,
        var status: Int?,
        var updatedAt: String?
    )
}

/**************************   get level api  response*********************/


data class GetLevelsApiResponse(
    var `data`: List<GetLevelsApiResponseData?>?,
    var message: String?,
    var status: Int?
) {
    data class GetLevelsApiResponseData(
        var _id: String?,
        var is_active: Int?,
        var level: String?,
        var userId: Any?
    )
}

/**************************   upload image api  response*********************/
data class UploadImageApiResponse(
    var `data`: UploadImageApiResponseData?,
    var message: String?,
    var status: Int?
) {
    data class UploadImageApiResponseData(
        var fileUrl: String?
    )
}

/**************************  card upload api  response*********************/

data class UploadCardApiResponse(
    var `data`: UploadCardApiResponseData?,
    var message: String?,
    var status: Int?
) {
    data class UploadCardApiResponseData(
        var __v: Int?,
        var _id: String?,
        var categoryId: String?,
        var createdAt: String?,
        var description: String?,
        var isOnline: Int?,
        var is_active: Int?,
        var levelId: String?,
        var location: Location?,
        var post_image: String?,
        var social_media: String?,
        var title: String?,
        var updatedAt: String?,
        var userId: String?
    ) {
        data class Location(
            var coordinates: List<Double?>?,
            var type: String?
        )
    }
}

/**************************  card data list  api  response*********************/

@Parcelize
data class CardDataListApiResponse(
    var `data`: List<CardDataListApiResponseData?>?,
    var links: Links?,
    var message: String?,
    var status: Int?
) : Parcelable{

    @Parcelize
    data class CardDataListApiResponseData(
        var __v: Int?,
        var _id: String?,
        var categoryDetail: CategoryDetail?,
        var categoryId: String?,
        var createdAt: String?,
        var description: String?,
        var isOnline: Int?,
        var is_active: Int?,
        var levelId: String?,
        var levelDetail: LevelsDetail?,
        var location: Location?,
        var mobile: String?,
        var post_image: String?,
        var notAccessible: Boolean?,
        var social_media: String?,
        var title: String?,
        var total_distance: Double?,
        var type: Int?,
        var updatedAt: String?,
        var userId: String?
    ) :Parcelable {

        @Parcelize
        data class CategoryDetail(
            var __v: Int?,
            var _id: String?,
            var adminId: String?,
            var category: String?,
            var category_image: String?,
            var category_name: String?,
            var category_short_name: String?,
            var createdAt: String?,
            var social_media: String?,
            var status: Int?,
            var updatedAt: String?
        ) :Parcelable

        @Parcelize
        data class LevelsDetail(
            var __v: Int?,
            var _id: String?,
            var createdAt: String?,
            var is_active: Int?,
            var level: String?,
            var updatedAt: String?
        ) :Parcelable

        @Parcelize
        data class Location(
            var coordinates: List<Double?>?,
            var type: String?
        ): Parcelable
    }

    @Parcelize
    data class Links(
        var currentPage: Int?,
        var total: Int?,
        var totalPages: Int?
    ) :Parcelable
}


/**************************  contact us  api  response*********************/

data class ContactUsApiResponse(
    var `data`: ContactUsApiResponseData?,
    var message: String?,
    var status: Int?
) {
    data class ContactUsApiResponseData(
        var __v: Int?,
        var _id: String?,
        var createdAt: String?,
        var is_active: Int?,
        var message: String?,
        var profile: Profile?,
        var updatedAt: String?,
        var userId: String?
    ) {
        data class Profile(
            var __v: Int?,
            var _id: String?,
            var createdAt: String?,
            var email: String?,
            var gender: Any?,
            var isAccountVerified: Boolean?,
            var level: String?,
            var location: Location?,
            var mobile: Any?,
            var role: String?,
            var updatedAt: String?,
            var user_name: String?,
            var user_status: Any?
        ) {
            data class Location(
                var coordinates: List<Double?>?,
                var type: String?
            )
        }
    }
}


data class RequestsSentApiResponse(
    var `data`: RequestsSentApiResponseData?,
    var message: String?,
    var status: Int?
) {
    data class RequestsSentApiResponseData(
        var __v: Int?,
        var _id: String?,
        var cardId: String?,
        var createdAt: String?,
        var is_active: Int?,
        var receiverId: String?,
        var senderId: String?,
        var type: Int?,
        var updatedAt: String?
    )
}

/**************************  like sent   api  response*********************/


//@Parcelize
//data class LikeSentApiResponse(
//    var `data`: List<LikeSentApiResponseData?>?,
//    var links: Links?,
//    var message: String?,
//    var status: Int?
//) :Parcelable {
//    @Parcelize
//    data class LikeSentApiResponseData(
//        var __v: Int?,
//        var _id: String?,
//        var card: Card?,
//        var cardId: String?,
//        var createdAt: String?,
//        var is_active: Int?,
//        var receiverId: String?,
//        var senderId: String?,
//        var type: Int?,
//        var updatedAt: String?,
//        var userDetail: UserDetail?
//    ) : Parcelable {
//
//        @Parcelize
//        data class Card(
//            var __v: Int?,
//            var _id: String?,
//            var categoryId: String?,
//            var createdAt: String?,
//            var description: String?,
//            var isOnline: Int?,
//            var is_active: Int?,
//            var levelId: String?,
//            var location: Location?,
//            var post_image: String?,
//            var social_media: String?,
//            var title: String?,
//            var updatedAt: String?,
//            var userId: String?
//        ) :Parcelable {
//
//            @Parcelize
//            data class Location(
//                var coordinates: List<Double?>?,
//                var type: String?
//            ): Parcelable
//        }
//
//        @Parcelize
//        data class UserDetail(
//            var __v: Int?,
//            var _id: String?,
//            var createdAt: String?,
//            var email: String?,
//            var gender: String?,
//            var isAccountVerified: Boolean?,
//            var level: String?,
//            var location: Location?,
//            var mobile: String?,
//            var password: String?,
//            var role: String?,
//            var updatedAt: String?,
//            var user_name: String?,
//            var user_status: Int?
//        ) : Parcelable{
//
//            @Parcelize
//            data class Location(
//                var coordinates: List<Double?>?,
//                var type: String?
//            ) :Parcelable
//        }
//    }
//
//    @Parcelize
//    data class Links(
//        var currentPage: Int?,
//        var total: Int?,
//        var totalPages: Int?
//    ) : Parcelable
//}


@Parcelize
data class LikeSentApiResponse(
    var `data`: List<LikeSentApiResponseData?>?,
    var links: Links?,
    var message: String?,
    var status: Int?
) : Parcelable {

    @Parcelize
    data class LikeSentApiResponseData(
        var __v: Int?,
        var _id: String?,
        var categoryDetail: CategoryDetail?,
        var categoryId: String?,
        var createdAt: String?,
        var description: String?,
        var isOnline: Int?,
        var is_active: Int?,
        var levelDetail: LevelDetail?,
        var requestId: String?,
        val mobile: String?,
        var type: Int?,
        var total_distance: Double?,
        var requestDate: String?,
        var levelId: String?,
        var location: Location?,
        var post_image: String?,
        var social_media: String?,
        var title: String?,
        var updatedAt: String?,
        var userId: String?
    ) : Parcelable {

        @Parcelize
        data class CategoryDetail(
            var __v: Int?,
            var _id: String?,
            var adminId: String?,
            var category: String?,
            var category_image: String?,
            var category_name: String?,
            var category_short_name: String?,
            var createdAt: String?,
            var social_media: String?,
            var status: Int?,
            var updatedAt: String?
        ) : Parcelable

        @Parcelize
        data class LevelDetail(
            var __v: Int?,
            var _id: String?,
            var createdAt: String?,
            var is_active: Int?,
            var level: String?,
            var updatedAt: String?
        ) : Parcelable

        @Parcelize
        data class Location(
            var coordinates: List<Double?>?,
            var type: String?
        ) : Parcelable
    }

    @Parcelize
    data class Links(
        var currentPage: Int?,
        var total: Int?,
        var totalPages: Int?
    ) : Parcelable
}


/**************************  like receive   api  response*********************/


//@Parcelize
//data class LikeReceiveApiResponse(
//    var `data`: List<LikeReceiveApiResponseData?>?,
//    var links: Links?,
//    var message: String?,
//    var status: Int?
//) : Parcelable {
//
//    @Parcelize
//    data class LikeReceiveApiResponseData(
//        var __v: Int?,
//        var _id: String?,
//        var card: Card?,
//        var cardId: String?,
//        var createdAt: String?,
//        var is_active: Int?,
//        var receiverId: String?,
//        var senderId: String?,
//        var type: Int?,
//        var updatedAt: String?,
//        var userDetail: UserDetail?
//    ) : Parcelable{
//
//        @Parcelize
//        data class Card(
//            var __v: Int?,
//            var _id: String?,
//            var categoryId: String?,
//            var createdAt: String?,
//            var description: String?,
//            var isOnline: Int?,
//            var is_active: Int?,
//            var levelId: String?,
//            var location: Location?,
//            var post_image: String?,
//            var social_media: String?,
//            var title: String?,
//            var updatedAt: String?,
//            var userId: String?
//        ) : Parcelable{
//
//            @Parcelize
//            data class Location(
//                var coordinates: List<Double?>?,
//                var type: String?
//            ) : Parcelable
//        }
//
//        @Parcelize
//        data class UserDetail(
//            var __v: Int?,
//            var _id: String?,
//            var createdAt: String?,
//            var email: String?,
//            var gender: String?,
//            var isAccountVerified: Boolean?,
//            var level: String?,
//            var location: Location?,
//            var mobile: String?,
//            var password: String?,
//            var role: String?,
//            var updatedAt: String?,
//            var user_name: String?,
//            var user_status: Int?
//        ) : Parcelable {
//
//            @Parcelize
//            data class Location(
//                var coordinates: List<Double?>?,
//                var type: String?
//            ) : Parcelable
//        }
//    }
//
//    @Parcelize
//    data class Links(
//        var currentPage: Int?,
//        var total: Int?,
//        var totalPages: Int?
//    ) :Parcelable
//}

@Parcelize
data class LikeReceiveApiResponse(
    var `data`: List<LikeReceiveApiResponseData?>?,
    var links: Links?,
    var message: String?,
    var status: Int?
) : Parcelable{

    @Parcelize
    data class LikeReceiveApiResponseData(
        var __v: Int?,
        var _id: String?,
        var categoryDetail: CategoryDetail?,
        var categoryId: String?,
        var createdAt: String?,
        var description: String?,
        var isOnline: Int?,
        var is_active: Int?,
        var levelDetail: LevelDetail?,
        var levelId: String?,
        var type: Int?,
        var mobile: String?,
        var requestId :String ?,
        var location: Location?,
        var total_distance: Double?,
        var requestDate: String?,
        var post_image: String?,
        var social_media: String?,
        var title: String?,
        var updatedAt: String?,
        var userId: String?
    ) : Parcelable {

        @Parcelize
        data class CategoryDetail(
            var __v: Int?,
            var _id: String?,
            var adminId: String?,
            var category: String?,
            var category_image: String?,
            var category_name: String?,
            var category_short_name: String?,
            var createdAt: String?,
            var social_media: String?,
            var status: Int?,
            var updatedAt: String?
        ) : Parcelable

        @Parcelize
        data class LevelDetail(
            var __v: Int?,
            var _id: String?,
            var createdAt: String?,
            var is_active: Int?,
            var level: String?,
            var updatedAt: String?
        ) : Parcelable

        @Parcelize
        data class Location(
            var coordinates: List<Double?>?,
            var type: String?
        ) : Parcelable
    }

    @Parcelize
    data class Links(
        var currentPage: Int?,
        var total: Int?,
        var totalPages: Int?
    ) : Parcelable
}

/**************************  request status   api  response*********************/
data class RequestStatusApiResponse(
    var `data`: RequestStatusApiResponseData?,
    var message: String?,
    var status: Int?
) {
    data class RequestStatusApiResponseData(
        var __v: Int?,
        var _id: String?,
        var cardId: String?,
        var createdAt: String?,
        var is_active: Int?,
        var receiverId: String?,
        var senderId: String?,
        var type: Int?,
        var updatedAt: String?
    )
}

/**************************  user setting   api  response*********************/
//data class SettingApiResponse(
//    var `data`: SettingApiResponseData?,
//    var message: String?,
//    var status: Int?
//) {
//    data class SettingApiResponseData(
//        var `data`: Data?,
//        var message: String?,
//        var status: Int?
//    ) {
//        data class Data(
//            var __v: Int?,
//            var _id: String?,
//            var createdAt: String?,
//            var email: String?,
//            var gender: Any?,
//            var isAccountVerified: Boolean?,
//            var isLocation: Boolean?,
//            var isNotification: Boolean?,
//            var level: String?,
//            var location: Location?,
//            var mobile: Any?,
//            var password: String?,
//            var role: String?,
//            var updatedAt: String?,
//            var user_name: String?,
//            var user_status: Any?
//        ) {
//            data class Location(
//                var coordinates: List<Double?>?,
//                var type: String?
//            )
//        }
//    }
//
//}


data class SettingApiResponse(
    var `data`: SettingApiResponseData?,
    var message: String?,
    var status: Int?
) {
    data class SettingApiResponseData(
        var __v: Int?,
        var _id: String?,
        var createdAt: String?,
        var email: String?,
        var gender: Any?,
        var isAccountVerified: Boolean?,
        var isLocation: Boolean?,
        var isNotification: Boolean?,
        var level: String?,
        var location: Location?,
        var mobile: Any?,
        var password: String?,
        var role: String?,
        var updatedAt: String?,
        var user_name: String?,
        var user_status: Any?
    ) {
        data class Location(
            var coordinates: List<Double?>?,
            var type: String?
        )
    }
}
/**************************  delete single card  api  response*********************/
data class DeleteCardApiResponse(
    var `data`: DeleteCardApiResponse?,
    var message: String?,
    var status: Int?
) {
    data class DeleteCardApiResponse(
        var __v: Int?,
        var _id: String?,
        var categoryId: String?,
        var createdAt: String?,
        var description: String?,
        var isOnline: Int?,
        var is_active: Int?,
        var level: Int?,
        var levelId: Any?,
        var location: Location?,
        var post_image: String?,
        var social_media: String?,
        var title: String?,
        var updatedAt: String?,
        var userId: String?
    ) {
        data class Location(
            var coordinates: List<Double?>?,
            var type: String?
        )
    }
}

@Parcelize
data class AcceptedStatusApiResponse(
    var `data`: List<AcceptedStatusApiResponseData?>?,
    var links: Links?,
    var message: String?,
    var status: Int?
) : Parcelable {

    @Parcelize
    data class AcceptedStatusApiResponseData(
        var __v: Int?,
        var _id: String?,
        var categoryDetail: CategoryDetail?,
        var categoryId: String?,
        var createdAt: String?,
        var description: String?,
        var isOnline: Int?,
        var is_active: Int?,
        var levelDetail: LevelDetail?,
        var total_distance: Double?,
        var levelId: String?,
        var location: Location?,
        var mobile: String?,
        var post_image: String?,
        var requestId: String?,
        var social_media: String?,
        var requestDate: String?,
        var title: String?,
        var type: Int?,
        var updatedAt: String?,
        var userId: String?
    ) : Parcelable{
        @Parcelize
        data class CategoryDetail(
            var __v: Int?,
            var _id: String?,
            var adminId: String?,
            var category: String?,
            var category_image: String?,
            var category_name: String?,
            var category_short_name: String?,
            var createdAt: String?,
            var social_media: String?,
            var status: Int?,
            var updatedAt: String?
        ) : Parcelable

        @Parcelize
        data class LevelDetail(
            var __v: Int?,
            var _id: String?,
            var createdAt: String?,
            var is_active: Int?,
            var level: String?,
            var updatedAt: String?
        ) : Parcelable

        @Parcelize
        data class Location(
            var coordinates: List<Double?>?,
            var type: String?
        ) : Parcelable
    }

    @Parcelize
    data class Links(
        var currentPage: Int?,
        var total: Int?,
        var totalPages: Int?
    ) : Parcelable
}


data class BackGroundApiResponse(
    var `data`: Data?,
    var message: String?,
    var status: Int?
) {
    data class Data(
        var __v: Int?,
        var _id: String?,
        var createdAt: String?,
        var updatedAt: String?,
        var userId: String?
    )
}

/**************************  card update  api  response*********************/

data class CardUpdateApiResponse(
    var `data`: Data?,
    var message: String?,
    var status: Int?
) {
    data class Data(
        var __v: Int?,
        var _id: String?,
        var categoryId: String?,
        var createdAt: String?,
        var description: String?,
        var isOnline: Int?,
        var is_active: Int?,
        var levelId: String?,
        var location: Location?,
        var mobile: String?,
        var post_image: String?,
        var social_media: String?,
        var title: String?,
        var updatedAt: String?,
        var userId: String?
    ) {
        data class Location(
            var coordinates: List<Double?>?,
            var type: String?
        )
    }
}