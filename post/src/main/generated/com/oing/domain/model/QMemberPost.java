package com.oing.domain.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberPost is a Querydsl query type for MemberPost
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberPost extends EntityPathBase<MemberPost> {

    private static final long serialVersionUID = -556523589L;

    public static final QMemberPost memberPost = new QMemberPost("memberPost");

    public final QBaseAuditEntity _super = new QBaseAuditEntity(this);

    public final NumberPath<Integer> commentCnt = createNumber("commentCnt", Integer.class);

    public final ListPath<MemberPostComment, QMemberPostComment> comments = this.<MemberPostComment, QMemberPostComment>createList("comments", MemberPostComment.class, QMemberPostComment.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath id = createString("id");

    public final StringPath imageUrl = createString("imageUrl");

    public final StringPath memberId = createString("memberId");

    public final DatePath<java.time.LocalDate> postDate = createDate("postDate", java.time.LocalDate.class);

    public final NumberPath<Integer> reactionCnt = createNumber("reactionCnt", Integer.class);

    public final ListPath<MemberPostReaction, QMemberPostReaction> reactions = this.<MemberPostReaction, QMemberPostReaction>createList("reactions", MemberPostReaction.class, QMemberPostReaction.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMemberPost(String variable) {
        super(MemberPost.class, forVariable(variable));
    }

    public QMemberPost(Path<? extends MemberPost> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberPost(PathMetadata metadata) {
        super(MemberPost.class, metadata);
    }

}

