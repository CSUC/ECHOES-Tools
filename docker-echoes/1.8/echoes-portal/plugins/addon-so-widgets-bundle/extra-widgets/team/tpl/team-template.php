<?php
$widget_title = wp_kses_post($instance['widget_title']);
$team_image = wp_get_attachment_image_src($instance['team_image'],'widgets_image_size');
$team_title = wp_kses_post($instance['team_title']);
$company_title = wp_kses_post($instance['company_title']);
$detail_box = wp_kses_post($instance['detail_box']);
$social_icon_size = wp_kses_post($instance['team_styling']['social_icon_size']);
?>



<?php if ($widget_title) { ?>
    <h3 class="widget-title">
        <span><?php echo $widget_title ?></span>
    </h3>
<?php } ?>


<div class="team">

<?php if ($team_image) { ?>
<div class="member_image">
<img src=" <?php echo $team_image[0]; ?>" alt="" />
</div>
<?php } ?>

<?php if ($team_title) { ?>
<h2 class="member_title"><?php  echo $team_title ; ?></h2>
<?php } ?>

<?php if ($company_title) { ?>
<h4 class="company_name"><?php  echo $company_title; ?></h4>
<?php } ?>

<?php if ($detail_box) { ?>
<div class="content">
<?php  echo $detail_box; ?>
</div>
<?php } ?>

<ul class="team_social">
<?php foreach( $instance['team_social_repeater'] as $j => $team_social_repeater ) : ?>
<li><a href="<?php  echo $team_social_repeater['team_social_url'] ?>" target="_blank"><?php echo siteorigin_widget_get_icon($team_social_repeater['team_social_icon']); ?></a></li>
<?php endforeach; ?>
</ul>

</div>





