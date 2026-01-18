package com.nt.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.nt.models.Comments;
import com.nt.models.Post;
import com.nt.repository.CommentRepository;
import com.nt.repository.PostRepository;

@Controller
public class PageController {

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private CommentRepository commentRepo;

    // ====== HOME PAGE (list posts + create post) ======
    @GetMapping("/")
    public String home(Model model) {
        List<Post> posts = postRepo.findAllByOrderByCreatedAtDesc();
        posts.forEach(p ->
            p.setCommentsCount(commentRepo.findByPostIdOrderByCreatedAtDesc(p.getId()).size())
        );
        model.addAttribute("posts", posts);
        return "home"; // home.html
    }

    // ====== CREATE POST ======
    @PostMapping("/posts")
    public String createPost(
            @RequestParam String username,
            @RequestParam String text,
            @RequestParam(required = false) MultipartFile image
    ) throws IOException {

        Post post = new Post();
        post.setUsername(username);
        post.setText(text);

        if (image != null && !image.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path path = Paths.get("uploads/" + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, image.getBytes());
            post.setImageUrl("/uploads/" + fileName);
        }

        postRepo.save(post);

        return "redirect:/"; // redirects to home page
    }

    // ====== LIKE POST ======
    @PostMapping("/posts/{id}/like")
    public String likePost(@PathVariable Long id) {
        Post post = postRepo.findById(id).orElseThrow();
        post.setLikes(post.getLikes() + 1);
        postRepo.save(post);
        return "redirect:/";
    }

    // ====== DELETE POST ======
    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        // Delete comments first
        List<Comments> comments = commentRepo.findByPostIdOrderByCreatedAtDesc(id);
        commentRepo.deleteAll(comments);

        // Delete post
        postRepo.deleteById(id);
        return "redirect:/";
    }

    // ====== EDIT POST PAGE ======
    @GetMapping("/posts/{id}/edit")
    public String editPostPage(@PathVariable Long id, Model model) {
        Post post = postRepo.findById(id).orElseThrow();
        model.addAttribute("post", post);
        return "edit-post"; // edit-post.html
    }

    // ====== UPDATE POST ======
    @PostMapping("/posts/{id}/edit")
    public String updatePost(
            @PathVariable Long id,
            @RequestParam String text
    ) {
        Post post = postRepo.findById(id).orElseThrow();
        post.setText(text);
        postRepo.save(post);
        return "redirect:/";
    }

    // ====== VIEW COMMENTS PAGE ======
    @GetMapping("/posts/{id}/comments")
    public String commentsPage(@PathVariable Long id, Model model) {
        Post post = postRepo.findById(id).orElseThrow();
        List<Comments> comments = commentRepo.findByPostIdOrderByCreatedAtDesc(id);

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        return "comments"; // comments.html
    }

    // ====== ADD COMMENT ======
    @PostMapping("/posts/{id}/comments/add")
    public String addComment(
            @PathVariable Long id,
            @RequestParam String username,
            @RequestParam String comment
    ) {
        Comments c = new Comments();
        c.setPostId(id);
        c.setUsername(username);
        c.setComment(comment);
        commentRepo.save(c);
        return "redirect:/posts/" + id + "/comments";
    }

    // ====== DELETE COMMENT ======
    @PostMapping("/comments/{id}/delete")
    public String deleteComment(@PathVariable Long id) {
        Comments comment = commentRepo.findById(id).orElseThrow();
        Long postId = comment.getPostId();
        commentRepo.deleteById(id);
        return "redirect:/posts/" + postId + "/comments";
    }
}
