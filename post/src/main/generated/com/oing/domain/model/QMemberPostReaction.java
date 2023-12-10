package com.oing.domain.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberPostReaction is a Querydsl query type for MemberPostReaction
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberPostReaction extends EntityPathBase<MemberPostReaction> {

    private static final long serialVersionUID = 845640452L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberPostReaction memberPostReaction = new QMemberPostReaction("memberPostReaction");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath ascii = createString("ascii");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath id = createString("id");

    public final StringPath memberId = createString("memberId");

    public final QMemberPost post;

    public QMemberPostReaction(String variable) {
        this(MemberPostReaction.class, forVariable(variable), INITS);
    }

    public QMemberPostReaction(Path<? extends MemberPostReaction> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberPostReaction(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberPostReaction(PathMetadata metadata, PathInits inits) {
        this(MemberPostReaction.class, metadata, inits);
    }

    public QMemberPostReaction(Class<? extends MemberPostReaction> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new QMemberPost(forProperty("post")) : null;
    }

}

