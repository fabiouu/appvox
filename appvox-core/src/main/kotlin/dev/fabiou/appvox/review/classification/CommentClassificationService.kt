package dev.fabiou.appvox.review.classification

import dev.fabiou.appvox.review.classification.CommentType.*

internal class CommentClassificationService {

    companion object {
        private const val LONG_REVIEW_THRESHOLD = 30

        private const val MIN_NEGATIVE_REVIEW_STAR = 1

        private const val MAX_NEGATIVE_REVIEW_STAR = 2

        private const val MIN_POSITIVE_REVIEW_STAR = 4

        private const val MAX_POSITIVE_REVIEW_STAR = 5
    }

    fun classifyComment(commentText: String, commentRating: Int): Set<CommentType> {
        val commentTypes = HashSet<CommentType>()
        val cleanCommentText = commentText.filter { !it.isWhitespace() }
        when {
            cleanCommentText.length > LONG_REVIEW_THRESHOLD -> commentTypes.add(LONG)
            cleanCommentText.length < LONG_REVIEW_THRESHOLD -> commentTypes.add(SHORT)
            commentRating in MIN_NEGATIVE_REVIEW_STAR..MAX_NEGATIVE_REVIEW_STAR -> commentTypes.add(NEGATIVE)
            commentRating in MIN_POSITIVE_REVIEW_STAR..MAX_POSITIVE_REVIEW_STAR -> commentTypes.add(POSITIVE)
        }
        return commentTypes
    }
}
