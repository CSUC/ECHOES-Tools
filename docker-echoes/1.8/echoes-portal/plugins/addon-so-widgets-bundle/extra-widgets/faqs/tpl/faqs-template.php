<?php
$widget_title = wp_kses_post($instance['widget_title']);
$query = siteorigin_widget_post_selector_process_query($instance['posts']);
$the_query = new WP_Query($query);

$toggle_top_border = wp_kses_post($instance['faqs_styling']['toggle_top_border']);
$toggle_right_border = wp_kses_post($instance['faqs_styling']['toggle_right_border']);
$toggle_bottom_border = wp_kses_post($instance['faqs_styling']['toggle_bottom_border']);
$toggle_left_border = wp_kses_post($instance['faqs_styling']['toggle_left_border']);
$title_bg = wp_kses_post($instance['faqs_styling']['title_bg']);

?>


<?php if ($widget_title) { ?>
    <h3 class="widget-title">
        <span><?php echo $widget_title ?></span>
    </h3>
<?php } ?>


<div class="soua-main">
        <?php while ($the_query->have_posts()) : $the_query->the_post(); ?>
        <div class="soua-accordion">
        <a class="soua-accordion-title">
        <div class="<?php if($toggle_top_border){ echo " top_b "; } if($toggle_right_border){ echo " right_b "; } if($toggle_bottom_border){ echo " bottom_b "; } if($toggle_left_border){ echo " left_b "; } if($title_bg){ echo " bg "; } ?>">
        <?php the_title(); ?>
		</div>
        </a>
        <div class="soua-accordion-content" <?php if(wp_kses_post($instance['faqs_styling']['content_bg_color'])): ?> style="padding: 15px";<?php endif; ?>><?php the_content();?></div>
        </div>
        <?php endwhile; ?>
</div> <!-- / accordion -->


