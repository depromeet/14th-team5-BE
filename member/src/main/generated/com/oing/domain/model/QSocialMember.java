package com.oing.domain.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSocialMember is a Querydsl query type for SocialMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSocialMember extends EntityPathBase<SocialMember> {

    private static final long serialVersionUID = 432806280L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSocialMember socialMember = new QSocialMember("socialMember");

    public final QBaseAuditEntity _super = new QBaseAuditEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath identifier = createString("identifier");

    public final QMember member;

    public final EnumPath<com.oing.domain.SocialLoginProvider> provider = createEnum("provider", com.oing.domain.SocialLoginProvider.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QSocialMember(String variable) {
        this(SocialMember.class, forVariable(variable), INITS);
    }

    public QSocialMember(Path<? extends SocialMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSocialMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSocialMember(PathMetadata metadata, PathInits inits) {
        this(SocialMember.class, metadata, inits);
    }

    public QSocialMember(Class<? extends SocialMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

