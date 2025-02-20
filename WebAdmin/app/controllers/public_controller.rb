class PublicController < ApplicationController
  def index
    @players = ExternalPlayer.all
    
    if params[:search].present?
      @players = @players.where("username ILIKE ? OR nickname ILIKE ?", 
                               "%#{params[:search]}%", 
                               "%#{params[:search]}%")
    end

    @players = case params[:sort]
      when 'username'
        @players.order(username: params[:direction] || 'asc')
      when 'rank'
        @players.order(rank: params[:direction] || 'asc')
      when 'playtime'
        @players.order(playtime: params[:direction] || 'desc')
      when 'first_join'
        @players.order(first_join: params[:direction] || 'desc')
      when 'last_seen'
        @players.order(last_seen: params[:direction] || 'desc')
      else
        @players
    end

    respond_to do |format|
      format.html
      format.json { render json: @players }
    end
  end
end
