package com.oing.domain.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberPostComment is a Querydsl query type for MemberPostComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberPostComment extends EntityPathBase<MemberPostComment> {

    private static final long serialVersionUID = 1282756132L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberPostComment memberPostComment = new QMemberPostComment("memberPostComment");

    public final QBaseAuditEntity _super = new QBaseAuditEntity(this);

    public final StringPath comment = createString("comment");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath id = createString("id");

    public final StringPath memberId = createString("memberId");

    public final QMemberPost post;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMemberPostComment(String variable) {
        this(MemberPostComment.class, forVariable(variable), INITS);
    }

    public QMemberPostComment(Path<? extends MemberPostComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberPostComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberPostComment(PathMetadata metadata, PathInits inits) {
        this(MemberPostComment.class, metadata, inits);
    }

    public QMemberPostComment(Class<? extends MemberPostComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new QMemberPost(forProperty("post")) : null;
    }

}

