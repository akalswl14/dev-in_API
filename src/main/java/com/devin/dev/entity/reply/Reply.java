package com.devin.dev.entity.reply;

import com.devin.dev.entity.base.ModifiedCreated;
import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Reply extends ModifiedCreated {

    @Id
    @GeneratedValue
    @Column(name = "reply_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "reply")
    private final List<ReplyLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "reply")
    private final List<ReplyImage> images = new ArrayList<>();

    private String content;

    @Enumerated
    private ReplyState state;

    private void setPost(Post post) {
        this.post = post;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setState(ReplyState state) {
        this.state = state;
    }

    public static Reply createReply(Post post, User user, String content) {
        Reply reply = new Reply();
        reply.setPost(post);
        reply.setUser(user);
        reply.setContent(content);
        reply.setState(ReplyState.VIEWABLE);

        return reply;
    }

    public static void setReplyImage(Reply reply, ReplyImage replyImage) {
        reply.images.add(replyImage);
        replyImage.setReply(reply);
    }

    public static Reply createReplyWithImages(Post post, User user, List<ReplyImage> images, String content) {
        Reply reply = new Reply();
        reply.setPost(post);
        reply.setUser(user);
        reply.setContent(content);
        reply.setState(ReplyState.VIEWABLE);

        for (ReplyImage image : images) {
            setReplyImage(reply, image);
        }

        return reply;
    }
}
