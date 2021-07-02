<?php
$query = siteorigin_widget_post_selector_process_query($instance['posts']);
$the_query = new WP_Query($query);
$widget_title = wp_kses_post($instance['widget_title']);
$read_more_text = wp_kses_post($instance['read_more_text']);
$layout = wp_kses_post($instance['layout']);
$excerpt_lengt = wp_kses_post($instance['excerpt_lenght']);
$title_linkable = wp_kses_post($instance['title_linkable']);
$image_linkable = wp_kses_post($instance['image_linkable']);
?>

<?php if ($widget_title) { ?>
    <h3 class="widget-title">
        <span><?php echo $widget_title ?></span>
    </h3>
<?php } ?>

<div class="all_main">
    <?php while ($the_query->have_posts()) : $the_query->the_post(); ?>

        <div class="auto_height <?php echo $layout ?>">
            <div class="blog_two_style clearfix">
                <div class="main">
                    <div class="image">
                        
                     <?php if(has_post_thumbnail()): ?>
                        <?php if($image_linkable): ?>
                         <a href="<?php the_permalink(); ?>"> <?php the_post_thumbnail('news-thumbnail');?> </a>
                         <?php else: ?>
                         <?php the_post_thumbnail('news-thumbnail'); ?>
                        <?php endif; ?>
                     <?php endif; ?>

                    </div>

                    <div class="content clearfix">
                    
                        <?php if($title_linkable): ?>
                        <h4><a href="<?php the_permalink(); ?>"><?php the_title(); ?></a></h4>
                        <?php else: ?>
                        <h4><?php the_title(); ?></h4>
                        <?php endif; ?>  
                        <?php
                        /*
                        ?>
                        <div class="meta">
                        by <?php the_author(); ?> | <?php echo get_the_date(); ?> | <?php the_category(', '); ?>
                        </div>
                        <?php
                        */
                        ?>
                        <div class="meta">
                            <?php echo get_the_date(); ?>
                        </div>

                        <div class="border"></div>
                        <?php echo excerpt($excerpt_lengt); ?><a href="<?php the_permalink(); ?>">...</a>
                        <div class="clearfix"></div>
                        <small><a href="<?php the_permalink(); ?>"><?php echo $read_more_text ?></a></small>
                    </div>
                </div>
            </div>
        </div>


    <?php endwhile; ?>

</div>

<div class="clearfix"></div>
