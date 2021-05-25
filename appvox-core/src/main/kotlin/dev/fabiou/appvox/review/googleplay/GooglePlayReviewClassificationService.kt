package dev.fabiou.appvox.review.googleplay

import dev.fabiou.appvox.review.classification.CommentType
import dev.fabiou.appvox.review.classification.UserPersona
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReviewResult
import java.time.Period
import dev.fabiou.appvox.review.googleplay.domain.GooglePlayReview;

internal class GooglePlayReviewClassificationService {

    private val LONG_REVIEW_THRESHOLD = 30

    private val MIN_NEGATIVE_REVIEW_STAR = 1

    private val MAX_NEGATIVE_REVIEW_STAR = 2

    private val MIN_POSITIVE_REVIEW_STAR = 4

    private val MAX_POSITIVE_REVIEW_STAR = 5

//    private val LOYAL_USER_THRESOLD = 6
//
//    fun defineUserPersona(reviews: List<GooglePlayReviewResult>): Set<UserPersona> {
//        val userPersonas = HashSet<UserPersona>()
//        val sortedReviews = reviews.sortedByDescending { it.userCommentTime }
//        sortedReviews.forEach { review ->
//            val reviewTypes = classifyReview(review)
//            if (reviewTypes.contains(ReviewType.LONG)) {
//                userPersonas.add(UserPersona.TALKATIVE)
//            }
//            if (reviewTypes.contains(ReviewType.SHORT)) {
//                userPersonas.add(UserPersona.QUIET)
//            }
//        }
//        val latestReviewType = classifyReview(sortedReviews.first())
//        if (latestReviewType.contains(ReviewType.NEGATIVE)) {
//            userPersonas.add(UserPersona.DETRACTOR)
//        }
//        if (latestReviewType.contains(ReviewType.POSITIVE)) {
//            userPersonas.add(UserPersona.PROMOTER)
//        }
//
//        val commentGap = Period.between(sortedReviews.first().userCommentTime, sortedReviews.last().userCommentTime)
//        val monthGap: Int = Math.abs(commentGap.getMonths())
//        if (monthGap >= LOYAL_USER_THRESOLD) {
//            userPersonas.add(UserPersona.LOYAL)
//        }
//
//        return userPersonas
//    }

    fun classifyComment(commentText: String, commentRating: Int): Set<CommentType> {
        val reviewTypes = HashSet<CommentType>()
        val cleanCommentText = commentText.filter { !it.isWhitespace() }
        when {
            cleanCommentText.length > LONG_REVIEW_THRESHOLD -> reviewTypes.add(CommentType.LONG)
            cleanCommentText.length < LONG_REVIEW_THRESHOLD -> reviewTypes.add(CommentType.SHORT)
            commentRating in MIN_NEGATIVE_REVIEW_STAR..MAX_NEGATIVE_REVIEW_STAR -> reviewTypes.add(CommentType.NEGATIVE)
            commentRating in MIN_POSITIVE_REVIEW_STAR..MAX_POSITIVE_REVIEW_STAR -> reviewTypes.add(CommentType.POSITIVE)
        }
        return reviewTypes
    }
}
