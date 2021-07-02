<?php
$widget_title =  wp_kses_post($instance['widget_title']);

$toggle_border = wp_kses_post($instance['toggle_styling']['toggle_border']);
$toggle_top_border = wp_kses_post($instance['toggle_styling']['toggle_top_border']);
$toggle_right_border = wp_kses_post($instance['toggle_styling']['toggle_right_border']);
$toggle_bottom_border = wp_kses_post($instance['toggle_styling']['toggle_bottom_border']);
$toggle_left_border = wp_kses_post($instance['toggle_styling']['toggle_left_border']);
$title_bg = wp_kses_post($instance['toggle_styling']['bg']);
?>


<?php if ($widget_title) { ?>
    <h3 class="widget-title">
        <span><?php echo $widget_title ?></span>
    </h3>
<?php } ?>


<div class="soua-main">
    <?php foreach( $instance['toggle_repeater'] as $i => $toggle_repeater ) : ?>
    <div class="soua-accordion">
        <a class="soua-accordion-title <?php echo $icon_selection; ?>">
        <div class="<?php if($toggle_top_border){ echo " top_b "; } if($toggle_right_border){ echo " right_b "; } if($toggle_bottom_border){ echo " bottom_b "; } if($toggle_left_border){ echo " left_b "; } if($title_bg){ echo " bg "; } ?>">
        
        <?php if($toggle_repeater['icon_position'] == 'left'):?>	
        <?php if(siteorigin_widget_get_icon( $toggle_repeater['toggle_icon'])): ?>
        <div class="soua-accordion-icon-left">
        <?php echo siteorigin_widget_get_icon( $toggle_repeater['toggle_icon'] ); ?>
        </div>
    	<?php endif; ?>
    	<?php endif; ?>

        <?php echo $toggle_repeater['toggle_title'] ?>

        <?php if($toggle_repeater['icon_position'] == 'right'):?>	
        <?php if(siteorigin_widget_get_icon( $toggle_repeater['toggle_icon'])): ?>
        <div class="soua-accordion-icon-right">
        <?php echo siteorigin_widget_get_icon( $toggle_repeater['toggle_icon'] ); ?>
        </div>
    	<?php endif; ?>
    	<?php endif; ?>

    	</div>
        </a>
        <div class="soua-accordion-content" <?php if(wp_kses_post($instance['toggle_styling']['content_bg_color'])): ?> style="padding: 15px";<?php endif; ?> > <?php echo $toggle_repeater['toggle_content'] ?></div>
    </div>
    <?php endforeach; ?>
</div><!-- / accordion -->

