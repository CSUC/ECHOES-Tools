<?php
$widget_title = wp_kses_post($instance['widget_title']);
$spacer = wp_kses_post($instance['spacer']);

?>



<?php if ($widget_title) { ?>
    <h3 class="widget-title">
        <span><?php echo $widget_title ?></span>
    </h3>
<?php } ?>


<div class="spacer" style="height: <?php echo $spacer ?>px">
</div>