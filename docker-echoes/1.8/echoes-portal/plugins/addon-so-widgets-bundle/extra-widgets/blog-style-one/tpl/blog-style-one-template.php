<?php
$query = siteorigin_widget_post_selector_process_query($instance['posts']);
$the_query = new WP_Query($query);
$widget_title = wp_kses_post($instance['widget_title']);
$read_more_text = wp_kses_post($instance['read_more_text']);
$alignment = wp_kses_post($instance['alignment']);
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

<div class="blog_list_main">
<?php while ($the_query->have_posts()) : $the_query->the_post(); ?>

<?php if($alignment == 'left'): ?>

    <div class="blog_one_style clearfix <?php echo $layout ?>">

         <div class="four columns image alpha">

         <?php if(has_post_thumbnail()): ?>

            <?php if($image_linkable): ?>
             <a href="<?php the_permalink(); ?>"> <?php the_post_thumbnail();?> </a>
             <?php else: ?>
             <?php the_post_thumbnail(); ?>
            <?php endif; ?>

         <?php endif; ?>

         </div>

        <div class="eight columns content">

        <?php if($title_linkable): ?>
        <h4><a href="<?php the_permalink(); ?>"><?php the_title(); ?></a></h4>
        <?php else: ?>
        <h4><?php the_title(); ?></h4>
        <?php endif; ?>    

        <?php echo excerpt($excerpt_lengt); ?><a href="<?php the_permalink(); ?>">...</a>
        </div>

        <div class="clearfix"></div>

        <div class="meta twelve columns clearfix">
            <div class="left-style">
            By <?php the_author(); ?> | <?php echo get_the_date(); ?> | <?php the_category(', '); ?>
            </div>
            <div class="right-style">
            <a href="<?php the_permalink(); ?>"><?php echo $read_more_text ?> </a>
            </div>
        </div>
    </div>

<?php elseif ($alignment == 'right'): ?>

<div class="blog_one_style clearfix <?php echo $layout ?>">

        <div class="eight columns content right_align">
        <?php if($title_linkable): ?>
        <h4><a href="<?php the_permalink(); ?>"><?php the_title(); ?></a></h4>
        <?php else: ?>
        <h4><?php the_title(); ?></h4>
        <?php endif; ?>    

        <?php echo excerpt($excerpt_lengt); ?><a href="<?php the_permalink(); ?>">...</a>
        </div>

         <div class="four columns image alpha">
        
         <?php if(has_post_thumbnail()): ?>

            <?php if($image_linkable): ?>
             <a href="<?php the_permalink(); ?>"> <?php the_post_thumbnail();?> </a>
             <?php else: ?>
             <?php the_post_thumbnail(); ?>
            <?php endif; ?>

         <?php endif; ?>

         </div>

        <div class="clearfix"></div>

        <div class="meta twelve columns clearfix">
            <div class="left-style">
            By <?php the_author(); ?> | <?php echo get_the_date(); ?> | <?php the_category(', '); ?>
            </div>
            <div class="right-style">
            <a href="<?php the_permalink(); ?>"><?php echo $read_more_text ?></a>
            </div>
        </div>
    </div>

<?php endif; ?>


<?php endwhile; ?>
</div>
