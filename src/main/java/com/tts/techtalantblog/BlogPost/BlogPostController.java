package com.tts.techtalantblog.BlogPost;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BlogPostController {

    @Autowired
    private BlogPostRepository blogPostRepository;

    private static List<BlogPost> posts = new ArrayList<>();
    
    @GetMapping(value = "/")
    public String index(BlogPost blogPost, Model model){

        posts.removeAll(posts);

        for (BlogPost post : blogPostRepository.findAll()) {
            posts.add(post);
        } 

        model.addAttribute("posts", posts);
        return "blogpost/index";
    }

    private BlogPost blogPost;

    @GetMapping(value = "/blogposts/new")
    public String newBlog(BlogPost blogPost) {
        return "blogpost/new";
    }

    @PostMapping(value = "/blogposts")
    public String addNewBlogPost(BlogPost blogPost, Model model) {
        blogPostRepository.save(blogPost);
        model.addAttribute("blogPost", blogPost);

        return "blogpost/result";
    }

    // Similar to @PostMapping or @GetMapping, but allows for @PathVariable 
    @RequestMapping(value = "/blogposts/{id}", method = RequestMethod.GET)
    // Spring takes whatever value is in {id} and passes it to our method params using @PathVariable
    public String editPostWithId(@PathVariable long id, BlogPost blogpost, Model model) {

        // findById() returns an Optional<T> which can be null, so we have to test
        Optional<BlogPost> post = blogPostRepository.findById(id);
        // Test if post actually exists
        if (post.isPresent()) {
            // Unwrap the post from Optional datatype 
            BlogPost actualPost = post.get();
            model.addAttribute("blogPost", actualPost);
        }
        return "blogpost/edit";
    }

    @RequestMapping(value = "/blogposts/update/{id}")
    public String updateExistingPost(@PathVariable long id, BlogPost blogPost, Model model){

        Optional<BlogPost> post = blogPostRepository.findById(id);
        if(post.isPresent()){
            BlogPost actualPost = post.get();
            actualPost.setTitle(blogPost.getTitle());
            actualPost.setAuthor(blogPost.getAuthor());
            actualPost.setBlogEntry(blogPost.getBlogEntry());
            // Save works for both creating new posts and overwriting them.
            // If the primary key of the Entity we give it matches an existing record, it will save over it. 
            blogPostRepository.save(actualPost);
            model.addAttribute("blogPost", actualPost);
        }

        return "blogpost/result";
    }

    @RequestMapping(value = "blogposts/delete/{id}")
    public String deletePostById(@PathVariable long id) {
        blogPostRepository.deleteById(id);

        return "blogpost/delete";
    }

}