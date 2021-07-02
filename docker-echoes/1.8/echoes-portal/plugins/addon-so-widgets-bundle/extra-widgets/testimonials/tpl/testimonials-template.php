<?php
$widget_title =  wp_kses_post($instance['widget_title']);
$layout_selection =  wp_kses_post($instance['layout_selection']);
$grid_selection =  wp_kses_post($instance['grid_selection']);
$query = siteorigin_widget_post_selector_process_query($instance['posts']);
$the_query = new WP_Query($query);
global $post;

?>


<?php if ($widget_title) { ?>
    <h3 class="widget-title">
        <span><?php echo $widget_title ?></span>
    </h3>
<?php } ?>



<div class="clearfix"></div>

<?php if($layout_selection == 'rotator'): ?>
<div class="testimonial_carousel">

        <?php  while ($the_query->have_posts()) : $the_query->the_post(); ?>



        <div class="testimonials_grid">
            <div class="testimonial_content big-col">
                <p><?php echo the_content(); ?></p>
            </div>
            <h5>
                <a href=" <?php
                $user_url = get_post_meta( $post->ID, '_cmb_testimonial_url', true );
                echo $user_url;
                ?>">
                    <?php
                    if ( has_post_thumbnail() ) { // check if the post has a Post Thumbnail assigned to it.
                        the_post_thumbnail('thumbnail', array( 'class' => 'img-responsive img-circle'));
                    }
                    ?>
                    <?php
                    $username = get_post_meta( $post->ID, '_cmb_testimonial_usermane', true );
                    echo $username;
                    ?>
                </a>
            </h5>
        </div>

    <?php endwhile; ?>
</div>

<?php elseif($layout_selection == 'grid'): ?>

    <?php  while ($the_query->have_posts()) : $the_query->the_post(); ?>

        <div class="margin_zero">
        <div class="<?php echo $grid_selection; ?>">
                <div class="testimonials_grid">
                    <div class="testimonial_content big-col">
                        <p><?php echo the_content(); ?></p>
                    </div>
                    <h5>
                        <a href=" <?php
                        $user_url = get_post_meta( $post->ID, '_cmb_testimonial_url', true );
                        echo $user_url;
                        ?>">
                            <?php
                            if ( has_post_thumbnail() ) { // check if the post has a Post Thumbnail assigned to it.
                                the_post_thumbnail('thumbnail', array( 'class' => 'img-responsive img-circle'));
                            }
                            ?>
                            <?php
                            $username = get_post_meta( $post->ID, '_cmb_testimonial_usermane', true );
                            echo $username;
                            ?>
                        </a>
                    </h5>
                </div>
        </div>
        </div>


    <?php endwhile; ?>
<?php endif; ?>



<div class="clearfix"></div>
