<?php
$widget_title = wp_kses_post($instance['widget_title']);
$divider = wp_kses_post($instance['divider_style']);
$divider_width = wp_kses_post($instance['border_width']);
$divider_color = wp_kses_post($instance['divider_color']);
$margin_top = wp_kses_post($instance['margin_top']);
$margin_bottom = wp_kses_post($instance['margin_bottom']);
?>



<?php if ($widget_title) { ?>
    <h3 class="widget-title">
        <span><?php echo $widget_title ?></span>
    </h3>
<?php } ?>

<hr class="so-divider" style="border-style:<?php echo $divider ?>; border-width:0 0 <?php echo $divider_width ?>px 0; border-color:<?php echo $divider_color; ?>; margin-top: <?php echo $margin_top ?>px; margin-bottom: <?php echo $margin_bottom ?>px; ">