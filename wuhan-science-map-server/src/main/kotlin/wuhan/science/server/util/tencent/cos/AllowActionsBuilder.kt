package wuhan.science.server.util.tencent.cos

object AllowActionsBuilder {
    private var allowActions: Array<String> = arrayOf()
    fun forPutObject(): AllowActionsBuilder {
        allowActions = allowActions.plus("name/cos:PutObject")
        return this
    }

    fun builder(): AllowActionsBuilder {
        allowActions = arrayOf()
        return this
    }

    fun forPutBucket(): AllowActionsBuilder {
        allowActions = allowActions.plus("name/cos:PutBucket")
        return this
    }

    fun forPostObject(): AllowActionsBuilder {
        allowActions = allowActions.plus("name/cos:PostObject")
        return this
    }
    fun forInitiateMultipartUpload(): AllowActionsBuilder {
        allowActions = allowActions.plus("name/cos:InitiateMultipartUpload")
        return this
    }
    fun forListMultipartUploads(): AllowActionsBuilder {
        allowActions = allowActions.plus("name/cos:ListMultipartUploads")
        return this
    }
    fun forListParts(): AllowActionsBuilder {
        allowActions = allowActions.plus("name/cos:ListParts")
        return this
    }
    fun forUploadPart(): AllowActionsBuilder {
        allowActions = allowActions.plus("name/cos:UploadPart")
        return this
    }
    fun forCompleteMultipartUpload(): AllowActionsBuilder {
        allowActions = allowActions.plus("name/cos:CompleteMultipartUpload")
        return this
    }
    fun forGetObject(): AllowActionsBuilder {
        allowActions = allowActions.plus("name/cos:GetObject")
        return this
    }

    fun build(): Array<String> {
        return allowActions
    }
}